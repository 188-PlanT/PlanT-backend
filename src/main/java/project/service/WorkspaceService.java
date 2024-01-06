package project.service;

import project.domain.*;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class WorkspaceService{
    
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
        
    // 성능 개선 필요
    @Transactional
    public Long makeWorkspace(CreateWorkspaceRequest request){
        
        String name = request.getName();
        //중복 이름 검증
        validateWorkspace(name);
        
        List<User> users = userRepository.findUsersByEmailList(request.getUsers());
        
        //Workspace 생성
        Workspace workspace = new Workspace(name, users);
        workspaceRepository.save(workspace);
        return workspace.getId();
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
    public Workspace updateWorkspace(Long id, CreateWorkspaceRequest request){
        Workspace workspace = workspaceRepository.findById(id)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        String name = request.getName();
        //중복 이름 검증
        if (!workspace.getName().equals(name)){
            validateWorkspace(name);
        }
        
        List<User> users = userRepository.findUsersByEmailList(request.getUsers());
        
        workspace.updateWorkspace(name, users);
        return workspace;
    }
    
    @Transactional
    public Workspace addUser(Long workspaceId, String userEmail){
        Workspace workspace = workspaceRepository.findById(workspaceId)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        User user = userRepository.findByEmail(userEmail)
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
    
    //bool 형으로 바꿔서 검증 함수 만들면 재사용성 더 좋을듯
    private void validateWorkspace(String name){
        if (workspaceRepository.existsByName(name)){
            throw new IllegalStateException("이미 존재하는 Workspace 입니다");
        }
    }
}