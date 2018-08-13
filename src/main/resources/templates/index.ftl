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
    <style>
        .listDiv{
            height: 160px;
            margin: 0 auto auto;
            width: 200px;
            vertical-align: middle;
            padding-top: 80px
        }
        .submit_loading {
            position:fixed;
            width:100%;
            height:100%;
            top:0;
            left:0;
            background-color:#000;
            text-align:center;
            opacity:0.8;
        }
        .loading_show {
            margin-top:15%;
        }
        .loading_context {
            color:#fff;
        }

    </style>
</head>
<body>
<#--模板下载-->
    <div class="listDiv">
        <button class="btn btn-large btn-block btn-info" onclick="downLoadModel()">点击下载关键字上传模板</button>
    </div>

<#--模板上传-->
    <div class="listDiv">
        <button class="btn btn-large btn-block btn-primary" onclick="uploadFile()">点击上传关键字文件</button>
        <p>提交后请耐心等待,会自动下载排名数据...</p>
    </div>

<#--关键字上传-->
    <div class="listDiv">
        <form id="fileFormId" name="fileForm" method="post" action="${base}/rank/search" enctype="multipart/form-data">
            <input id="fileId" style="display: none" type="file" name="file" onchange="submitForm()"/>
        </form>
    </div>

<#--loading-->
    <div id="loading" class="submit_loading" style="display:none">
        <div class="loading_show">
            <img src="${request.contextPath}/static/img/loading.gif">
            <p class="loading_context">请等待。。。</p>
        </div>
    </div>

<script>
    function uploadFile(){
        $("#fileId").click();
    }

    function submitForm(){
        //点击按钮提交表单
        //document.getElementById("loading").style.display = "";
        $("#fileFormId").submit();
        //document.getElementById("loading").style.display = "none";
        //$("#loading").style.display = "";
    }

    function downLoadModel(){
        window.location.href = "${base}/rank/downLoadModel";
    }

  /*  $(function(){
        Event.on("#fileFormId", "submit", function(e){
            DOM.show("#loading");
        })

    })*/

    // $(function(){
    //     document.onreadystatechange = completeLoading;
    // })
    // function completeLoading(){
    //     if (document.readyState == "complete") {
    //         document.getElementById("loading").style.display = "none";
    //     }
    // }



</script>
</body>
</html>