<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="UTF-8">
<title>uploadAjax</title>

</head>
<style>
	.uploadResult {
        width: 100%;
        background-color: gray;
    }

    .uploadResult ul {
        display: flex;
        flex-flow: row;
        justify-content: center;
        align-items: center;
    }

    .uploadResult ul li {
        list-style: none;
        padding: 10px;
    }

    .uploadResult ul li img {
        width: 20px;
    }
</style>
<body>
	<h1>Upload with Ajax</h1>
	<div class='uploadDiv'>
		<input type='file' name='uploadFile' multiple>
	</div>

	<div class="uploadResult">
		<ul>

		</ul>
	</div>
	<button id='uploadBtn'>Upload</button>

	<script src="https://code.jquery.com/jquery-3.3.1.min.js"
		integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
		crossorigin="anonymous">
		
	</script>

</body>
<script>
	$(document).ready(function() {

		var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$"); //확장자명 검사
		var maxSize = 5242880; //5MB

		function checkExtension(fileName, fileSize) {
			if (fileSize >= maxSize) {
				alert("파일 사이즈 초과");
				return false;
			}

			if (regex.test(fileName)) {
				alert("해당 종류의 파일은 업로드할 수 없습니다.");
				return false;
			}
			return true;
		}

		var cloneObj = $(".uploadDiv").clone(); //파일을 선택하기 전의 상태, 초기상태를 복제해 놓는다.
		
		var uploadResult = $(".uploadResult ul");
			function showUploadedFile(uploadResultArr) {
				var str = "";

				$(uploadResultArr).each(
					function(i, obj) {
						if(!obj.image) {
							str += "<li><img src='resources/img/attach.png'>"
								+ obj.fileName + "</li>";
						} else {
							// str += "<li>" + obj.fileName + "</li>";
							var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_"
							+ obj.uuid + "_" + obj.fileName);

							str += "<li><img src='/display?fileName="+fileCallPath+"'><li>";
						}	
					});

				uploadResult.append(str);
			}

		$("#uploadBtn").on("click", function(e) {
			var formData = new FormData();
			var inputFile = $("input[name='uploadFile']");
			var files = inputFile[0].files;
			console.log(files);

			//add filedate to formdata
			for (var i = 0; i < files.length; i++) {
				if (!checkExtension(files[i].name, files[i].size)) {
					return false;
				}

				formData.append("uploadFile", files[i]);
			}

			$.ajax({
				url : '/uploadAjaxAction', // uploadAjaxAction 경로로 설정되어 있는 곳으로 보내주
				processData : false,
				contentType : false,
				data : formData,
					type : 'POST',
					dataType: 'json',
					success : function(result) {
						console.log("Uploaded", result);

						showUploadedFile(result);

						$(".uploadDiv").html(cloneObj.html()); //초기상태의 html의 내용을 가지고 와서 .uploadDiv를 초기화 
					},
					error : function(e) {
						console.log("Error", e);
					}
			}); //$.ajax
		});
	});
</script>
</html>