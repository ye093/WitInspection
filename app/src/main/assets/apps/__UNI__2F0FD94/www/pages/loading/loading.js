
      !(function(){
        var uniAppViewReadyCallback = function(){
          setCssToHead([".",[1],"_button{width:80%; margin:",[0,15]," 10%;}\n",])();
document.dispatchEvent(new CustomEvent("generateFuncReady", { detail: { generateFunc: $gwx('./pages/loading/loading.wxml') } }));
        }
        if(window.__uniAppViewReady__){
          uniAppViewReadyCallback()
        }else{
          document.addEventListener('uniAppViewReady',uniAppViewReadyCallback)
        }
      })();
      