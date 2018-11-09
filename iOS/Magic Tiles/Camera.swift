//
//  Camera.swift
//  Magic Tiles
//
//  Created by Ando RANDRIAMARO on 09/11/2018.
//  Copyright © 2018 Hajanirina Randimbisoa. All rights reserved.
//

import Foundation
import UIKit
import AVFoundation

class Camera: UIViewController {
    
    @IBOutlet weak var cameraView: UIView!
    
    //Declaration des variables utiles pour le camera
    var captureSession: AVCaptureSession?
    var videoPreviewLayer: AVCaptureVideoPreviewLayer?
    var frontCamera = AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: .front)
    var backCamera = AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: .back)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if #available(iOS 10.2, *) {
            let captureDevice = AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: .back)
            do{
                let input = try AVCaptureDeviceInput(device: captureDevice!)
                captureSession = AVCaptureSession()
                captureSession?.addInput(input)
                videoPreviewLayer = AVCaptureVideoPreviewLayer(session: captureSession!)
                videoPreviewLayer?.frame = view.layer.bounds
                cameraView.layer.addSublayer(videoPreviewLayer!)
                captureSession?.startRunning()
            }
            catch{
                print("error")
            }
        }
        
    }
    //Fonction pour utiliser la camera frontal
    func switchToFrontCamera() {
        if frontCamera?.isConnected == true {
            captureSession?.stopRunning()
            let captureDevice = AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: .front)
            do{
                let input = try AVCaptureDeviceInput(device: captureDevice!)
                captureSession = AVCaptureSession()
                captureSession?.addInput(input)
                videoPreviewLayer = AVCaptureVideoPreviewLayer(session: captureSession!)
                videoPreviewLayer?.frame = view.layer.bounds
                cameraView.layer.addSublayer(videoPreviewLayer!)
                captureSession?.startRunning()
                
            }
            catch{
                print("error")
            }
        }
    }
    //Fonction pour utiliser la camera principale
    func switchToBackCamera() {
        if backCamera?.isConnected == true {
            captureSession?.stopRunning()
            let captureDevice = AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: .back)
            do{
                let input = try AVCaptureDeviceInput(device: captureDevice!)
                captureSession = AVCaptureSession()
                captureSession?.addInput(input)
                videoPreviewLayer = AVCaptureVideoPreviewLayer(session: captureSession!)
                videoPreviewLayer?.frame = view.layer.bounds
                cameraView.layer.addSublayer(videoPreviewLayer!)
                captureSession?.startRunning()
                
            }
            catch{
                print("error")
            }
        }
    }
    
    @IBAction func backToHomeBtn(_ sender: UIButton) {
        print("back to home")
        self.performSegue(withIdentifier: "ToHomeSegue", sender: self)
    }
    
    @IBAction func imageCapture(_ sender: Any) {
        
    }
    
    @IBAction func rotateCamera(_ sender: Any) {
        guard let currentCameraInput: AVCaptureInput = captureSession?.inputs.first else{
            return
        }
        if let input = currentCameraInput as? AVCaptureDeviceInput {
            if input.device.position == .back {
                switchToFrontCamera()
            }
            if input.device.position == .front{
                switchToBackCamera()
            }
            
        }
    }
    
    @IBAction func flashButton(_ sender: Any) {
    }
    
    override func didReceiveMemoryWarning() {
    }
}
