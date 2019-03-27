
      !(function(){
        var uniAppViewReadyCallback = function(){
          setCssToHead(["body{background:#F6F7F8;}\n",])();
document.dispatchEvent(new CustomEvent("generateFuncReady", { detail: { generateFunc: $gwx('./pages/shoppingcart/shoppingcart.wxml') } }));
        }
        if(window.__uniAppViewReady__){
          uniAppViewReadyCallback()
        }else{
          document.addEventListener('uniAppViewReady',uniAppViewReadyCallback)
        }
      })();
      