package com.seungho.good;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class UploadController {

	@GetMapping("/uploadForm")
	public void uploadForm() {

		log.info("upload form");
	}

	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) { // 배열과 모델을 인자로 받아준다.
		String uploadFolder = "C:\\upload";

		for (MultipartFile multipartFile : uploadFile) {
			log.info("--------------------");
			log.info("Upload File Name : " + multipartFile.getOriginalFilename());
			log.info("Upload File size : " + multipartFile.getSize());

			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());

			try {
				multipartFile.transferTo(saveFile); // 설정한 경로에 파일 저장

			} catch (Exception e) {
				log.error(e.getMessage());
			}

		}
	}

	@GetMapping("/uploadAjax")
	public void uploadAjax() {

		log.info("uploadAjax");
	}

	// 폴더 생성 method
	private String getFolder() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date date = new Date();

		String str = sdf.format(date);

		return str.replace("-", File.separator);
	}

	@PostMapping("/uploadAjaxAction")
	public void uploadAjaxPost(MultipartFile[] uploadFile) {

		log.info("Update Ajax POST...............");

		String uploadFolder = "C:\\upload";

		String uploadFolderPath = getFolder();

		// make folder --------
		File uploadPath = new File(uploadFolder, uploadFolderPath); // upload 밑에 현재날짜를 기반으로 폴더를 만들어주세용

		if (uploadPath.exists() == false) // 파일이 존재하지 않다면
			uploadPath.mkdirs(); // 날짜 기반으로 파일을 만들어 주십시오
		// make yyyy/MM/dd folder

		for (MultipartFile multipartFile : uploadFile) {
			log.info("--------------------------------------");
			log.info("Upload File Name: " + multipartFile.getOriginalFilename());
			log.info("Upload File Size: " + multipartFile.getSize());

			String uploadFileName = multipartFile.getOriginalFilename();

			log.info("자르기전 파일 이름: " + uploadFileName);
			// IE has file path
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			log.info("only file name: " + uploadFileName);

			UUID uuid = UUID.randomUUID(); // 랜덤값을 형성함
			uploadFileName = uuid.toString() + "_" + uploadFileName; // 랜덤값_filename 으로 이름을 지어 같은 파일이 들어오더라도 파일이 덮어씌워지지않게 한다.

//			File saveFile = new File(uploadFolder, uploadFileName);
			File saveFile = new File(uploadPath, uploadFileName); // 날짜를 기반으로 만들어진 폴더 안에 사용자의 파일이 저장되어진다.

			try {
				multipartFile.transferTo(saveFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		}
	}
}
