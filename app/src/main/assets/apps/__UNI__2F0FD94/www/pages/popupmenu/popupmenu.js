
      !(function(){
        var uniAppViewReadyCallback = function(){
          setCssToHead([".",[1],"grace-popup-menu{background:#4c4c4c; width:",[0,300],"; padding:6px; right:0px; top:0px; position:absolute; z-index:9999; border-radius:5px;}\n.",[1],"grace-popup-mask{background:rgba(255,255,255, 0); width:100%; height:100%; position:fixed; left:0; top:0; z-index:9998;}\n.",[1],"demo-btn-1{width:28px; height:28px; float:right; border-radius:3px; border:1px solid #00C777; text-align:center; font-size:0;}\n.",[1],"demo-btn-1 wx-image{width:18px; height:18px; margin:5px;}\n.",[1],"grace-list wx-view{color:#FFF;}\n.",[1],"grace-list .",[1],"title{border:0;}\n",])();
document.dispatchEvent(new CustomEvent("generateFuncReady", { detail: { generateFunc: $gwx('./pages/popupmenu/popupmenu.wxml') } }));
        }
        if(window.__uniAppViewReady__){
          uniAppViewReadyCallback()
        }else{
          document.addEventListener('uniAppViewReady',uniAppViewReadyCallback)
        }
      })();
      