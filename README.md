<img src="https://github.com/WorkingYashSharma/MunchePartner/blob/main/assets/SafeShip%20Restaurant%20Partner%20Cover%20Page.png">

### Show some ❤️ and star the repo to show support for the project

<h2 align="center"> Munche Restaurant Partner Android App </h2>

This is the Munche Restaurant Partner app which is part of the Munche Customer Facing App, users can add new restaurants and accept/decline orders from the users.Setupt their UPI Payment and their restaurant's location.

# ✨ Features
- Users can add and edit menu items for different categories.
- Take payment using UPI or COD.
- Accurately setup their restaurant's location.
- Enable/disable menu items individually.
- Enable/disable their restaurant.
- and more...

# 🗒️ ToDo

- Complete the Sales page so the users can see their daily/monthly earnings.
- Build a delivery funnel and send push notifications to customers notifying them about their order status.
- Have a dedicated chat functionality.

# 📚 Major Libraries Used

- Firebase Suite - For Auth, Database and Storage
- Glide - For Image Loading.
- Lottie - To Display native `json` animations in android.

# 💻 Build Instructions

1. Clone or download the repository :

```shell
git clone https://github.com/WorkingYashSharma/MunchePartner.git
```

2. Import the project into Android Studio.

3. Create a firebase project and add this android app to the project.

4. Run the below command in the terminal to get your **SHA-1** key and upload it in the project settings in your firebase console, without this you cannot authenticate users using their phone numbers.

```shell
keytool -exportcert -list -v \
-alias androiddebugkey -keystore ~/.android/debug.keystore
```

5. Enable Phone Number sign in Firebase Authentication Tab in the left side.

6. Download and add the `google-services.json` file from the firebase project you created earlier and add it to the project under **app** folder.

7. Run the project into an emulator or a physical device.

# 👨 Made By

`Yash Sharma`

# 👓 Also Checkout

<a href="https://github.com/WorkingYashSharma/MuncheUser">Munche User App</a>
