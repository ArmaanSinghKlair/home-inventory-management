let menu = document.getElementsByClassName("hamburger-menu")[0];
let openButton = document.getElementById("open-hamburger-menu");
let closeButton = document.getElementById("close-hamburger-menu");

   
   openButton.addEventListener("click",e=>{
        menu.style.left = "0%";
   });
   
   closeButton.addEventListener("click",e=>{
        menu.style.left = "200%";
   });
   