
      !(function(){
        var uniAppViewReadyCallback = function(){
          setCssToHead([".",[1],"grace-items-demo1{background:#00B26A;}\n.",[1],"grace-items-demo2{background:#F76260;}\n",])();
document.dispatchEvent(new CustomEvent("generateFuncReady", { detail: { generateFunc: $gwx('./pages/scroll/scroll.wxml') } }));
        }
        if(window.__uniAppViewReady__){
          uniAppViewReadyCallback()
        }else{
          document.addEventListener('uniAppViewReady',uniAppViewReadyCallback)
        }
      })();
      