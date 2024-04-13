const registerBtn = document.getElementById("registerBtn").addEventListener("click", (ev) => {
  ev.preventDefault()
  const name = document.getElementById("name").value
  const mail = document.getElementById("mail").value
  const login = document.getElementById("login").value
  const password = document.getElementById("password").value
  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/

  if (name !== "" && password !== "" && login !== "" && mail !== "") {
    if (emailRegex.test(mail) == true) {
      fetch("http://localhost:8080/v1/users/register", {
        method: "post",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name: name,
          mail: mail,
          login: login,
          password: password,
        }),
      })
        .then((response) => {
          if (!response.ok) {
            return response.json().then((error) => {
              throw new Error(error.error)
            })
          }
        })
        .then((data) => {
          window.location.href = "/front/login.html"
        })
        .catch((error) => {
          showErrorMessage("Verifique os campos...")
        })
    } else {
      showErrorMessage("E-mail inválido.")
    }
  }
})

function showErrorMessage(message) {
  let errorMessageDiv = document.getElementById("errorMessage")
  let errorMessageText = document.getElementById("errorMessageText")
  errorMessageText.innerText = message
  errorMessageDiv.style.display = "block"
}
