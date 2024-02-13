package com.seungho.good;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.seungho.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

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

	@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody 
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {

//		log.info("Update Ajax POST...............");

		List<AttachFileDTO> list = new ArrayList<AttachFileDTO>();		
		String uploadFolder = "C:\\upload";

		String uploadFolderPath = getFolder();

		// make folder --------
		File uploadPath = new File(uploadFolder, uploadFolderPath); // upload폴더에 현재날짜를 기준으로 폴더생성

		if (uploadPath.exists() == false) // 파일이 존재하지 않다면
			uploadPath.mkdirs(); // 날짜 기반으로 파일을 만들어 주십시오
		// make yyyy/MM/dd folder

		for (MultipartFile multipartFile : uploadFile) {
			
			AttachFileDTO attachDTO = new AttachFileDTO();
			
//			log.info("--------------------------------------");
//			log.info("Upload File Name: " + multipartFile.getOriginalFilename());
//			log.info("Upload File Size: " + multipartFile.getSize());
			
			String uploadFileName = multipartFile.getOriginalFilename();

			// IE has file path
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			log.info("only file name: " + uploadFileName);

			attachDTO.setFileName(uploadFileName);
			
			UUID uuid = UUID.randomUUID(); // 랜덤값을 형성함
			uploadFileName = uuid.toString() + "_" + uploadFileName; // 랜덤값_filename 으로 이름을 지어 같은 파일이 들어오더라도 난수를 붙여서 다른 파일로 인식되게 한다.

			try {
				File saveFile = new File(uploadPath, uploadFileName); // 날짜를 기반으로 만들어진 폴더 안에 사용자의 파일이 저장되어진다.
				multipartFile.transferTo(saveFile);
				
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);
				
				//check file type image
				if(checkImageType(saveFile)) {

					attachDTO.setImage(true);
						
					FileOutputStream thumnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumnail, 100, 100); //Thumbnail 생성

					log.info("Check Image : " + thumnail);
					thumnail.close();
				}
				//add to List
				list.add(attachDTO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		}  //end for
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	// 폴더 생성 method
	private String getFolder() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date date = new Date();

		String str = sdf.format(date);

		return str.replace("-", File.separator);
	}

	// 이미지 파일 판단 method
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());

			return contentType.startsWith("image"); // "접두어로 image가 오는지 검사"
			
		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}
}
