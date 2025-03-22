package project.common.constant;

public class MailContant {

    public static String VERIFICATION_MAIL_SUBJECT = "[PLANT] 이메일 인증 메일입니다";

    public static String VERIFICATION_MAIL_CONTENT =
            """
        <div style='font-family: IBM Plex Sans KR; width: 600px; height: 400px; \
        border: 4px solid #134074; margin: 50px auto; box-sizing: border-box; border-radius: 8px;'>
            <div style='display: flex; align-items: center'>
                <a href='%s'>
                    <img style='width: 34px; height: 50px; padding-left: 20px; padding-top: 20px; padding-right: 10px' \
                    src='https://plant-s3.s3.ap-northeast-2.amazonaws.com/logo.png' />
                </a>
                <h1><span style='color: #134074; font-size: 36px; font-family: IBM Plex Sans KR; font-weight: 800; \
                word-wrap: break-word'>PLAN,T 이메일 주소 인증</span></h1>
            </div>
            <p style='line-height: 26px; margin-top: 10px; padding: 0 20px; color: black; font-size: 18px; \
            font-family: IBM Plex Sans KR; font-weight: 500;'>
                서비스 이용을 위해 이메일 주소 인증이 필요합니다.<br/>
                아래 인증 번호를 입력하여 인증을 완료해주세요.
            </p>
            <h1 style='margin: 0; padding: 0 20px;'>
                <span style='color: #134074; font-size: 32px; font-family: IBM Plex Sans KR; font-weight: 800; \
                word-wrap: break-word'>%d</span>
            </h1>
        </div>
        """;
}
