package project.BBolCha;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.BBolCha.global.model.CustomResponseEntity;

@RestController
public class VersionController {
    // version 확인
    @GetMapping("version")
    public CustomResponseEntity<String> version() {
        return CustomResponseEntity.success("version 2.0.0");
    }

}
