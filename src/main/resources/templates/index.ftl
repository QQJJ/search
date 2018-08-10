<#assign base = request.contextPath />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
    <link href="${request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="${request.contextPath}/static/css/flat-ui.css" rel="stylesheet">
    <script src="${request.contextPath}/static/js/jquery.min.js"></script>
    <script src="${request.contextPath}/static/js/flat-ui.min.js"></script>
    <script src="${request.contextPath}/static/js/application.js"></script>
</head>
<body>
<#--模板下载-->
    <div>
        <button onclick="downLoadModel()">点击下载关键字上传模板</button>
    </div>

<#--文件下载-->
    <div>
        <form id="fileFormId" name="fileForm" method="post" action="${base}/rank/search" enctype="multipart/form-data">
            <input type="file" name="file"/>
            <input onclick="submitForm()" type="button" value="点击上传"/>
        </form>
    </div>
<script>

    function submitForm(){
        //点击按钮提交表单
        $("#fileFormId").submit();
        alert("文件已经提交,请等待");
    }

    function downLoadModel(){
        window.location.href = "${base}/rank/downLoadModel";
    }

</script>
</body>
</html>