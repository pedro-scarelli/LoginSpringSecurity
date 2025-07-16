package com.login.user.util;

import java.util.Objects;

import com.login.user.config.ApiEnvVariablesConfig;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class ActivateUserEmailBody {

    private final String apiBaseUrl;

    private static final String TEMPLATE = """
        <div style="background-color:#f5f5f5; margin:0; padding:20px 0;">
          <div style="max-width:600px; margin:0 auto;">
            <table style="width:100%%; background:#fff; border-radius:8px; border-collapse:separate;">
              <tr>
                <td style="background-color:#1A237E; padding:24px 32px; border-radius:8px 8px 0 0; text-align:center;">
                  <h1 style="color:#fff; margin:0; font-weight:700; font-size:24px;">
                    Bem-vindo à Login API – Spring Security
                  </h1>
                </td>
              </tr>
              <tr>
                <td style="padding:32px 24px; text-align:center;">
                  <p style="color:#181818; margin:0 0 16px; font-size:16px; line-height:1.6;">
                    Obrigado por se cadastrar! Clique no botão abaixo para ativar sua conta:
                  </p>
                  <a href="%s/activate/%s"
                     style="display:inline-block; background-color:#1A237E; color:#fff;
                            text-decoration:none; border-radius:4px; padding:12px 24px;
                            font-size:16px; font-weight:600;">
                    Ativar Conta
                  </a>
                </td>
              </tr>
              <tr>
                <td style="padding:24px; text-align:center; font-size:12px; color:#666;">
                  Se o botão não funcionar, copie e cole este link no seu navegador:<br>
                  <a href="%s/activate/%s" style="color:#1A237E; word-break:break-all;">
                    %s/activate/%s
                  </a>
                </td>
              </tr>
            </table>
          </div>
        </div>
        """;

    public ActivateUserEmailBody(@Valid ApiEnvVariablesConfig config) {
        apiBaseUrl = config.baseUrl();
    }

    public String of(String userId) {
        Objects.requireNonNull(userId, "userId não pode ser nulo");
        String base = apiBaseUrl + "/v1/user";

        return String.format(TEMPLATE,
                             base, userId,
                             base, userId,
                             base, userId);
    }
}
