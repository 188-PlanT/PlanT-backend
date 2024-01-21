package project.service;

import project.domain.*;
import project.common.auth.oauth.UserInfo;
import project.dto.workspace.*;
import project.repository.WorkspaceRepository;
import project.repository.UserRepository;
import project.exception.user.NoSuchUserException;
import project.exception.workspace.NoSuchWorkspaceException;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import static java.util.stream.Collectors.toList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceService{
    
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
        
    @Transactional
    public Workspace makeWorkspace(CreateWorkspaceRequest request, Long createUserId){
                
        User createUser = userRepository.findById(createUserId)
            .orElseThrow(NoSuchUserException::new);
        
        List<User> userList = userRepository.findByIdIn(request.getUsers());
        
        validateUserList(request.getUsers(), userList);

        Workspace workspace = Workspace.builder()
                                        .name(request.getName())
                                        .profile(request.getProfile())
                                        .user(createUser)
                                        .build();
        
        workspace.addUserByList(userList);        
        workspaceRepository.save(workspace);
        
        return workspace;
    }
    
    @Transactional
    public void removeWorkspace(Long id){
        Workspace findWorkspace = workspaceRepository.findById(id)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        workspaceRepository.delete(findWorkspace);
    }
    
    public Workspace findOne(Long id){
        return workspaceRepository.findById(id)
            .orElseThrow(NoSuchWorkspaceException::new);
    }
    
    public Page<Workspace> findAll(Pageable pageable){
        return workspaceRepository.findAll(pageable);
    }

    @Transactional
    public Workspace updateWorkspace(Long id, Long userId, UpdateWorkspaceRequest request){
        
        User loginUser = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
        Workspace workspace = workspaceRepository.findById(id)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        workspace.checkAdmin(loginUser);
        
        workspace.updateWorkspace(request.getName(), request.getProfile());
        return workspace;
    }
    
    @Transactional
    public Workspace addUser(Long workspaceId, Long userId){
        Workspace workspace = workspaceRepository.findById(workspaceId)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        User user = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
        workspace.addUser(user);
        return workspace;
    }
    
    @Transactional
    public void removeUser(Long workspaceId, Long userId){
        Workspace workspace = workspaceRepository.findById(workspaceId)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        User user = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
        workspace.removeUser(user);
    }
    
    @Transactional
    public void changeUserAuthority(Long workspaceId, Long userId, UserRole authority){
        Workspace workspace = workspaceRepository.findById(workspaceId)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        User user = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
        workspace.giveAuthority(user, authority);
    }
    
    
    // private void validateWorkspaceName(String name){
    //     if (workspaceRepository.existsByName(name)){
    //         throw new IllegalStateException("이미 존재하는 Workspace 입니다");
    //     }
    // }
    
    private void validateUserList(List<Long> userIds, List<User> userList){
        if (userIds.size() != userList.size()){
            throw new NoSuchUserException();
        }
    }
    
}