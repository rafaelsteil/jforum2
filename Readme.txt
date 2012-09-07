Bugfixes
1. if user sees "CAPTCHA is not ready", he need to request new one. Otherwise lucky spammers happily post without CAPTCHA
2. Few HTML fixes for post view and edit form

New features
1. Poster's IP address is set as concatenation of all forwarded and actual IP addres
2. Antispam solution using unique URI for posts which is set by JavaScript ("Javascript obfuscated URL for posting" in settings).
If value is not empty, default URL is not handled and error is shown.
3. Following languages are added: ru_RU, lv_LV.

Infrastructure
1. MySQL script changed from TYPE table option to ENGINE option.
1. Default container encoding changed to UTF-8

