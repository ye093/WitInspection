
      !(function(){
        var uniAppViewReadyCallback = function(){
          setCssToHead([".",[1],"badge-demo{-webkit-box-pack:center;-webkit-justify-content:center;-ms-flex-pack:center;justify-content:center; }\n.",[1],"badge-demo wx-text{margin:",[0,6],";}\n",])();
document.dispatchEvent(new CustomEvent("generateFuncReady", { detail: { generateFunc: $gwx('./pages/badge/badge.wxml') } }));
        }
        if(window.__uniAppViewReady__){
          uniAppViewReadyCallback()
        }else{
          document.addEventListener('uniAppViewReady',uniAppViewReadyCallback)
        }
      })();
      