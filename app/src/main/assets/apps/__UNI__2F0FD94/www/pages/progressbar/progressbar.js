
      !(function(){
        var uniAppViewReadyCallback = function(){
          setCssToHead([".",[1],"_progress{margin:12px 0;}\n",])();
document.dispatchEvent(new CustomEvent("generateFuncReady", { detail: { generateFunc: $gwx('./pages/progressbar/progressbar.wxml') } }));
        }
        if(window.__uniAppViewReady__){
          uniAppViewReadyCallback()
        }else{
          document.addEventListener('uniAppViewReady',uniAppViewReadyCallback)
        }
      })();
      