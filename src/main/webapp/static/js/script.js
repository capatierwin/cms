var cg_members = document.getElementById("cg-members"),
    members = document.getElementById("members");
cg_members.addEventListener("click", function() {
    members.style.display = "none" === members.style.display ? "block" : "none"
});