
      !(function(){
        var uniAppViewReadyCallback = function(){
          setCssToHead(["body{background:#F5F6F7;}\n.",[1],"colorYellow{color:#F4711E !important;}\n",])();
document.dispatchEvent(new CustomEvent("generateFuncReady", { detail: { generateFunc: $gwx('./pages/table/table.wxml') } }));
        }
        if(window.__uniAppViewReady__){
          uniAppViewReadyCallback()
        }else{
          document.addEventListener('uniAppViewReady',uniAppViewReadyCallback)
        }
      })();
      