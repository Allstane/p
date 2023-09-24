package world.library.templates

case object RegT extends Template {

  val content: String =
    s"""
       |<script>
       |  function createJson() {
       |    const regForm = document.getElementById("regForm");
       |    regForm.addEventListener("submit", (e) => {
       |    e.preventDefault();
       |    const formData = new FormData(regForm);
       |    const l = formData.get('login');
       |    const p = formData.get('password');
       |    const em = formData.get('email');
       |    const regs = {login: l, password: p, email: em, firstname: null,
       |                  surname: null, tg: null, favBooks: null, origlang: null, langs: null, location: null};
       |    const json = JSON.stringify(regs);
       |    fetch('/registration', {
       |      method: 'POST',
       |      headers: {  'Content-Type': 'application/json' },
       |      body: json }).then((response) => {  console.log(response); location.href = "/auth"; })
       |                                       });
       |                            }
       |</script>
       |
       |<form id="regForm" action="/registration" method="get">
       |  <div>
       |    <label for="login">Enter your login: </label>
       |    <input type="text" name="login" id="name" required />
       |  </div>
       |  <div>
       |    <label for="password">Enter your pass: </label>
       |    <input type="text" name="password" id="name" required />
       |  </div>
       |  <div>
       |    <label for="email">Enter your email: </label>
       |    <input type="email" name="email" id="email" required />
       |  </div>
       |  <div>
       |    <input type="submit" onclick="createJson()" value="Register" />
       |  </div>
       |</form>
       |""".stripMargin

  val currentHtml: String = html(content)
}
