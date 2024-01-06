package project.api;

import project.domain.*;
import project.dto.devLog.*;
import project.exception.devLog.*;
import project.service.DevLogService;
import project.repository.DevLogRepository;

import java.util.List;
import static java.util.stream.Collectors.toList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
@RequiredArgsConstructor
public class DevLogController {
    
    private final DevLogService devLogService;
    private final DevLogRepository devLogRepository;
    
    @GetMapping("/devLogs")
    public ResponseEntity<DevLogFindAllResponse> findAllDevlogs( 
                                Pageable pageable,
                                @RequestParam(value="scheduleId", required = false) Long scheduleId,
                                @RequestParam(value="accountId", required = false) String accountId){
        
        Page<DevLog> page = devLogService.findAllBySearch(pageable, scheduleId, accountId);
        
        List <DevLogDto> responseData = page.getContent()
            .stream()
            .map(DevLogDto::new)
            .collect(toList());
        
        DevLogFindAllResponse response = new DevLogFindAllResponse(page.getTotalPages(), page.getNumber(), responseData);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/devLogs")
    public ResponseEntity<Long> createDevLog(@RequestBody CreateDevLogRequest createDevLogRequest){
        
        Long devLogId = devLogService.createDevLog(createDevLogRequest);
        
        return ResponseEntity.ok(devLogId);
    }
    
    @GetMapping("/devLogs/{devLogId}")
    public ResponseEntity<DevLogDto> findSingleDevLog(@PathVariable("devLogId") Long devLogId){
        DevLog devLog = devLogRepository.findById(devLogId)
            .orElseThrow(NoSuchDevLogException::new);
        
        DevLogDto response = new DevLogDto(devLog);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/devLogs/{devLogId}")
    public ResponseEntity<DevLogDto> updateDevLog(@PathVariable("devLogId") Long devLogId,
                                                 @RequestBody UpdateDevLogRequest request){
        
        DevLog devLog = devLogService.updateDevLog(devLogId, request.getContent());
        
        DevLogDto response = new DevLogDto(devLog);
            
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/devLogs/{devLogId}")
    public ResponseEntity<DeleteDevLogResponse> deleteSingleDevLog(@PathVariable("devLogId") Long devLogId){
        
        devLogService.deleteDevLog(devLogId);
        
        return ResponseEntity.ok(new DeleteDevLogResponse());
    }
}