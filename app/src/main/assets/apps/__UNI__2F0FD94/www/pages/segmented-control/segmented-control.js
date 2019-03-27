
      !(function(){
        var uniAppViewReadyCallback = function(){
          setCssToHead(["body{background:#FFF;}\n.",[1],"demo-content{width:100%; height:200px; background:#F7F8F9; text-align:center; line-height:200px; font-size:22px;}\n",])();
document.dispatchEvent(new CustomEvent("generateFuncReady", { detail: { generateFunc: $gwx('./pages/segmented-control/segmented-control.wxml') } }));
        }
        if(window.__uniAppViewReady__){
          uniAppViewReadyCallback()
        }else{
          document.addEventListener('uniAppViewReady',uniAppViewReadyCallback)
        }
      })();
      