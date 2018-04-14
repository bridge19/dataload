
	$("#findfile").click(function(){
		alert("aa");
		var projectName=document.getElementById("warName").value
		var fileName=document.getElementById("fileName").value
		var logDate=document.getElementById("logDate").value
		
		$.get("/user/getFileList?projectName="+projectName+"&fileName="+fileName+"&logDate="+logDate,function(data,status){
			if(data.success){
				var files=data.data;
				files.forEach(showFile);
			}
		  });
	});
	
	function showFile(item){
		$("#fileList").append(item);
	}
	
	$("#openfile").click(function(){
		$.get("/user/loadData",function(data,status){
			$("#part1").html(data);
		  });
	});
