package project.dto.workspace;

import lombok.Getter;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;
import project.domain.*;
import static java.util.stream.Collectors.toList;

@Getter
@AllArgsConstructor
public class FindWorkspaceUsersResponse{
    private Long id;
    private String name;
    
    // private List<SimpleUserDto> users = new ArrayList<> ();
        
    // public FindWorkspaceUsersResponse(Workspace workspace){
    //     this.id = workspace.getId();
    //     this.name = workspace.getName();
        
    //     users.addAll(
    //         workspace.getUserWorkspaces().stream()
    //             .map(userWorkspace -> new SimpleUserDto(userWorkspace.getUser()))
    //             .collect(toList())
    //     );
    // }
    
    // @Getter
    //  static class SimpleUserDto{
    //      private Long userId;
    //      private String userEmail;
    //      private String userName;
         
    //      public SimpleUserDto(User user){
    //          this.userId = user.getId();
    //          this.userEmail = user.getEmail();
    //          this.userName = user.getName();
    //      }
    //  }   
}