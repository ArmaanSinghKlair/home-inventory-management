let sections = [];
sections.push(document.getElementById("users"));
sections.push(document.getElementById("add-users"));
sections.push(document.getElementById("categories"));
sections.push(document.getElementById("add-categories"));
sections.push(document.getElementById("promote-users"));
sections.push(document.getElementById("demote-users"));

let ctas = [];
ctas.push(document.getElementById("users-cta"));
ctas.push(document.getElementById("categories-cta"));
ctas.push(document.getElementById("promote-demote-cta"));


window.onload = function(){
    sections[2].style.display="none";
    sections[3].style.display="none";
    sections[4].style.display="none";
    sections[5].style.display="none";
    // So that sections show up on click
ctas.forEach(e=>{
    e.addEventListener("click", ()=>{
        sections.forEach((s)=>{
            if(e.getAttribute("id").startsWith(s.getAttribute("id")) || e.getAttribute("id").startsWith(s.getAttribute("id").substring(4)))
                s.style.display = "block";
            else
                s.style.display = "none";
        });
    });
});
ctas[2].addEventListener("click",()=>{
        sections.forEach((e)=>{
            if(e.getAttribute("id").startsWith("promote") || e.getAttribute("id").startsWith("demote"))
                e.style.display = "block";
            else
                e.style.display = "none";
        });
    })
}
