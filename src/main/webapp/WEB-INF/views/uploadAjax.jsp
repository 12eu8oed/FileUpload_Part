<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="UTF-8">
<title>uploadAjax</title>

</head>
<body>
	<h1>Upload with Ajax</h1>
	<div class='uploadDiv'>
		<input type='file' name='uploadFile' multiple>
	</div>
	<button id='uploadBtn'>Upload</button>

	<script src="https://code.jquery.com/jquery-3.3.1.min.js"
	integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" 
	crossorigin="anonymous">
	</script>

</body>
<script>
	$(document).ready(function() {
		
		var regex = new RegExp("(.*?)\.(exe|sh|zip|alx)$");
		var maxSize = 5242880; //5MB
		
		function checkExtension(fileName, fileSize)
		{
			if(fileSize >= maxSize)
			{
				alert("파일 사이즈 초과");
				return false;
			}
			
			if(regex.test(fileName))
			{
				alert("해당 종류의 파일은 업로드할 수 없습니다.");
				return false;
			}
			return true;
		}
		
		$("#uploadBtn").on("click", function(e) {
			var formData = new FormData();
			var inputFile = $("input[name='uploadFile']");
			var files = inputFile[0].files;
// 			console.log(formData);
// 			console.log(inputFile);
			console.log(files);

			//add filedate to formdata
			for(var i=0; i<files.length; i++){
				formData.append("uploadFile", files[i]);
			}
			
			$.ajax({
				url: '/uploadAjaxAction', // uploadAjaxAction 경로로 설정되어 있는 곳으로 보내주
				processData: false, 
				contentType: false, 
				data: formData,
				type: 'POST',
				success: function(result){
					alert("Uploaded");
				},
			    error: function(e){
			        console.log("Error", e);
			    }
			}); //$.ajax
		});
	});
</script>
</html>