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
    public void removeWorkspace(Long workspaceId, Long userId){
        User loginUser = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
        Workspace findWorkspace = workspaceRepository.findById(workspaceId)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        findWorkspace.checkAdmin(loginUser);
        
        workspaceRepository.delete(findWorkspace);
    }
    
    @Transactional(readOnly = true)
    public Workspace findOne(Long workspaceId, Long userId){
        User loginUser = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
        Workspace findWorkspace = workspaceRepository.findById(workspaceId)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        findWorkspace.checkUser(loginUser);
        
        // Lazy Loding
        findWorkspace.getUserWorkspaces().stream()
            .forEach(uw -> {
                // User u = uw.getUser();
                // Long id = u.getId();
                log.info("user = {}", uw.getUser());
                log.info("userId = {}", uw.getUser().getId());
            });
        
        return findWorkspace;
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
    public Workspace addUser(Long workspaceId, Long loginUserId, Long userId){
        User loginUser = userRepository.findById(loginUserId)
            .orElseThrow(NoSuchUserException::new);
        
        Workspace workspace = workspaceRepository.findById(workspaceId)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        workspace.checkUser(loginUser);
        
        User user = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
        workspace.addUser(user);
        // Lazy Loding
        workspace.getUserWorkspaces().stream()
            .forEach(uw -> {
                log.info("user = {}", uw.getUser());
                log.info("userId = {}", uw.getUser().getId());
            });
        
        return workspace;
    }
    
    @Transactional
    public void removeUser(Long workspaceId, Long loginUserId, Long userId){
        User loginUser = userRepository.findById(loginUserId)
            .orElseThrow(NoSuchUserException::new);
        
        Workspace workspace = workspaceRepository.findById(workspaceId)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        workspace.checkAdmin(loginUser);
        
        User user = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
        workspace.removeUser(user);
    }
    
    @Transactional
    public Workspace changeUserAuthority(Long workspaceId, Long loginUserId, Long userId, UserRole authority){
        User loginUser = userRepository.findById(loginUserId)
            .orElseThrow(NoSuchUserException::new);
        
        Workspace workspace = workspaceRepository.findById(workspaceId)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        User user = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
        //스스로 관리자 권한 부여 가능 -> 로직 수정 필요
        if(loginUser != user){
            workspace.checkAdmin(loginUser);
        }        
        
        workspace.giveAuthority(user, authority);
        
        // Lazy Loding
        workspace.getUserWorkspaces().stream()
            .forEach(uw -> {
                log.info("user = {}", uw.getUser());
                log.info("userId = {}", uw.getUser().getId());
            });
        
        return workspace;
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