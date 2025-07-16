package com.login.user.util;

import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class RedefinePasswordEmailBody {

    private static final String TEMPLATE = """
        <div style="background-color: rgb(245, 245, 245); margin: 0; padding: 20px 0;">
          <div style="max-width: 600px; margin: 0 auto;">
            <table style="width:100%%;background:#fff;border-radius:8px;border-collapse:separate;">
              <tr>
                <td style="background-color: #1A237E; padding: 24px 32px; border-radius:8px 8px 0 0; text-align:center;">
                  <h1 style="color:#fff; margin:0; font-weight:700; font-size:24px; line-height:1.5;">
                    Login API
                  </h1>
                </td>
              </tr>
              <tr>
                <td style="text-align:center; padding:32px 24px;">
                  <h1 style="color:#181818; margin:0 0 16px; font-weight:700; font-size:24px; line-height:1.5;">
                    Token de redefinição de senha
                  </h1>
                  <p style="color:#666; margin:0; font-size:16px; line-height:1.6;">
                    Use o código abaixo para redefinir sua senha.<br>
                    Válido por <strong>5 minutos</strong>
                  </p>
                </td>
              </tr>
              <tr>
                <td style="padding:0 24px 24px;">
                  <div style="background-color:#ECE3FB;border-radius:8px;padding:24px;text-align:center;">
                    <p style="color:#181818; margin:0; font-weight:700; font-size:32px; letter-spacing:2px;">
                      %s
                    </p>
                  </div>
                </td>
              </tr>
              <tr>
                <td style="text-align:center; padding:0 24px 32px;">
                  <div style="color:#666; font-size:14px;">
                    ⚠️ Não compartilhe este token com ninguém
                  </div>
                </td>
              </tr>
            </table>
          </div>
        </div>
        """;

    public String of(String otpCode) {
        Objects.requireNonNull(otpCode, "userId não pode ser nulo");

        return String.format(TEMPLATE, otpCode);
    }
}
