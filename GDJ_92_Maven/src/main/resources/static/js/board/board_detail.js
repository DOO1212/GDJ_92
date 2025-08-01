/*const actions = document.querySelectorAll('.action');*/

/*actions.forEach(el => {
	el.addEventListener('click', function () {
		console.log('click')
	})
});*/

/*const actions1 = document.querySelectorAll('.action');

for(a of actions1) {
	a.addEventListener("click", function (e) {
		let k = e.target;
		let kind = k.getAttribute("data-kind");
		const frm = document.getElementById("frm");
		
		if(kind == 'd') {
			frm.setAttribute("method", "POST")
			frm.setAttribute("action", "./delete")
		}else {
			frm.setAttribute("action", "./update")
			
		}
	});
}*/

const elements = document.querySelectorAll('.action');
const form = document.querySelector('#frm');
elements.forEach(el => {
	el.addEventListener('click', () => {
		const data = el.getAttribute('data-kind');
		if (data === 'd') {
			form.setAttribute('action', './delete');
			form.setAttribute('method', 'post');
			form.submit();
		} else if (data === 'u'){
			form.setAttribute('action', './update');
			form.submit();						
		}
	});
});