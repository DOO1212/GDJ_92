/**
 * 
 */

const pn = document.querySelectorAll(".pn");
const pn2 = document.getElementsByClassName("pn")
const searchForm = document.getElementById("searchForm")
const pageNum = document.querySelector("#pageNum")

pn.forEach(function(item){
	item.addEventListener("click", function(){
		let n = this.getAttribute("data-pn")
		pageNum.value=n;
		searchForm.submit();
	})
});