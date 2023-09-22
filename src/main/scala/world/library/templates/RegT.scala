package world.library.templates

case object RegT extends Template {
  val content: String =
    s"""
       |<script>
       |  function createJson() {
       |  console.log('Start of a fun');
       |    let loginForm = document.getElementById("regForm");
       |    loginForm.addEventListener("submit", (e) => {
       |    e.preventDefault();
       | // Получаем данные из формы
       |  const formData = new FormData(regForm);
       |
       |  // Отправляем данные на сервер
       |  fetch('/registration', {
       |    method: 'POST',
       |    body: formData,
       |  })
       |    .then((response) => {
       |      // Обрабатываем ответ от сервера
       |      console.log(response);
       |    })
       |    .catch((error) => {
       |      // Обрабатываем ошибку
       |      console.error(error);
       |    });
       |});
       |  }
       |</script>
       |
       |<form id="regForm" action="/registration" method="get">
       |  <div>
       |    <label for="name">Enter your name: </label>
       |    <input type="text" name="name" id="name" required />
       |  </div>
       |  <div>
       |    <label for="email">Enter your email: </label>
       |    <input type="email" name="email" id="email" required />
       |  </div>
       |  <div>
       |    <input type="submit" onclick="createJson()" value="Subscribe!" />
       |  </div>
       |</form>
       |""".stripMargin

  val currentHtml: String = html(content)
}
