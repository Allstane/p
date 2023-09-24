package world.library.templates

case object AuthT extends Template {

  val content: String =
    s"""
       |<script>
       |  function createJson() {
       |    const regForm = document.getElementById("authForm");
       |    regForm.addEventListener("submit", (e) => {
       |    e.preventDefault();
       |    const formData = new FormData(authForm);
       |    const l = formData.get('login');
       |    const p = formData.get('password');
       |    const creds = {login: l, password: p};
       |    const json = JSON.stringify(creds);
       |    fetch('/authentication', {
       |      method: 'POST',
       |      headers: {  'Content-Type': 'application/json' },
       |      body: json }).then((response) => {  console.log(response); location.href = "/"; })
       |                                       });
       |                            }
       |</script>
       |
       |<form id="authForm" action="/authentication" method="get">
       |  <div>
       |    <label for="login">Enter your login: </label>
       |    <input type="text" name="login" id="name" required />
       |  </div>
       |  <div>
       |    <label for="password">Enter your pass: </label>
       |    <input type="text" name="password" id="name" required />
       |  </div>
       |  <div>
       |    <input type="submit" onclick="createJson()" value="Log in" />
       |  </div>
       |</form>
       |""".stripMargin

  val currentHtml: String = html(content)
}
