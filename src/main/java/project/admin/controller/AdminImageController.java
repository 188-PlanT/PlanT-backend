package project.admin.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
public class AdminImageController{

	@GetMapping("/admin/image/test")
	public String uploadForm(){
		return "admin/image/image-upload-form";
	}
}