// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorPluginCameraParameters",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "CapacitorPluginCameraParameters",
            targets: ["cameraParametersPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", branch: "main")
    ],
    targets: [
        .target(
            name: "cameraParametersPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/cameraParametersPlugin"),
        .testTarget(
            name: "cameraParametersPluginTests",
            dependencies: ["cameraParametersPlugin"],
            path: "ios/Tests/cameraParametersPluginTests")
    ]
)