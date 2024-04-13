const sendBtn = document.getElementById("sendBtn").addEventListener("click", (ev) => {
  ev.preventDefault()
  const username = document.getElementById("username").value
  const password = document.getElementById("password").value

  if (username !== "" && password !== "") {
    fetch("http://localhost:8080/v1/users/login", {
      method: "post",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        login: username,
        password: password,
      }),
    })
      .then((response) => response.json())
      .then((data) => {
        const token = data.token
        localStorage.setItem("jwt-token", token)
        window.location.href = "/front/users.html"
      })
      .catch(() => {
        showErrorMessage()
      })
  }
})

function showErrorMessage(message) {
  let errorMessageDiv = document.getElementById("errorMessage")
  let errorMessageText = document.getElementById("errorMessageText")
  errorMessageText.innerText = "Usuário ou senha incorretos."
  errorMessageDiv.style.display = "block"
}
