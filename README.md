## Create your own keys

1. You can follow the command below to create your own key (you can also modify the parameters appropriately according to your own habits):

    ```shell
    openssl genrsa -des3 -out biliturbo.key 2048
    openssl req -sha256 -new -x509 -days 36500 -key biliturbo.key -out biliturbo.crt -subj "/C=CN/ST=SC/L=CD/O=BiliTurbo/OU=study/CN=BiliTurbo"
    openssl pkcs8 -topk8 -nocrypt -inform PEM -outform DER -in biliturbo.key -out biliturbo_private.der
    ```
    You will get three files: a `key` file, a `crt` file and a `der` file.
2. Go to your BiliTurbo running directory, create a new folder named `cert`, copy the `key` file you just generated into this directory and rename it to `certificate.key`, and copy the `der` file into this directory and rename it for `private.der`.
3. Install the `crt` file into your computer's `Trusted Root Certification Authorities` certificate store.
4. Emjoy!