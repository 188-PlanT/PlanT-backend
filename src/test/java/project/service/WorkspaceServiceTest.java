package project.service;

import project.repository.UserRepository;
import project.repository.WorkspaceRepository;
import project.repository.ScheduleRepository;
import project.domain.*;
import project.dto.workspace.*;
import project.exception.user.*;
import project.exception.workspace.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class WorkspaceServiceTest{
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private WorkspaceRepository workspaceRepository;
    
    @Mock
    private ScheduleRepository scheduleRepository;
    
    @InjectMocks
    private WorkspaceService workspaceService;
    
    @Test
    public void 워크스페이스_제작(){
        //given
        User admin = new User("admin@gmail.com", "admin", "test1111", "profile", UserRole.ADMIN);
        User user1 = new User("test11@gmail.com", "test11", "test1111", "profile", UserRole.USER);
        User user2 = new User("test22@gmail.com", "test22", "test2222", "profile", UserRole.USER);
        
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        
        List<Long> userIdList = new ArrayList<>(Arrays.asList(1L, 2L));
        
        CreateWorkspaceRequest successRequest = new CreateWorkspaceRequest("testWorkspace1", "profile1", userIdList);
        
        given(userRepository.findByEmail("admin@gmail.com")).willReturn(Optional.of(admin));
        given(userRepository.findByEmail("failEmail@gmail.com")).willReturn(Optional.ofNullable(null));
        given(userRepository.findByIdIn(any(List.class))).willReturn(userList);
        
        //when
        Workspace result = workspaceService.makeWorkspace(successRequest, "admin@gmail.com");
        
        //then
        assertEquals(result.getName(), "testWorkspace1");
        
        assertThrows(NoSuchUserException.class, () -> {
            workspaceService.makeWorkspace(successRequest, "failEmail@gmail.com");
        });
    }
    
    @Test
    public void 워크스페이스_삭제(){
        //given
        User admin = new User("admin@gmail.com", "admin", "test1111", "profile", UserRole.ADMIN);

        Workspace workspace = Workspace.builder()
                                        .name("testWorksapce1")
                                        .user(admin)
                                        .build();
        
        given(userRepository.findByEmail("admin@gmail.com")).willReturn(Optional.of(admin));
        given(userRepository.findByEmail("failEmail@gmail.com")).willReturn(Optional.ofNullable(null));
        given(workspaceRepository.findById(1L)).willReturn(Optional.of(workspace));
        given(workspaceRepository.findById(2L)).willReturn(Optional.ofNullable(null));
        
        //when
        workspaceService.removeWorkspace(1L, "admin@gmail.com");
        
        //then
        assertThrows(NoSuchUserException.class, () -> {
            workspaceService.removeWorkspace(1L, "failEmail@gmail.com");
        });
        assertThrows(NoSuchWorkspaceException.class, () -> {
            workspaceService.removeWorkspace(2L, "admin@gmail.com");
        });
    }
}