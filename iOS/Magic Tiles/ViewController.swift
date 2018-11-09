//
//  ViewController.swift
//  Piano Tiles
//
//  Created by Hajanirina Randimbisoa on 20/10/2018.
//  Copyright © 2018 Hajanirina Randimbisoa. All rights reserved.
//

import UIKit
import AVFoundation
import MapKit
import CoreLocation

class ViewController: UIViewController, AVAudioPlayerDelegate, UISearchDisplayDelegate, UISearchBarDelegate {
    
    @IBOutlet weak var bp_play: UIButton!
    @IBOutlet weak var more: UIButton!
    @IBOutlet weak var exit: UIButton!
    @IBOutlet weak var score: UIButton!
    @IBOutlet weak var playButton: UIButton!
    @IBOutlet weak var cameraButton: UIButton!
    
    var buttonsAreOn = true
    var bp_playButtonCenter: CGPoint!
    var exitButtonCenter: CGPoint!
    var scoreButtonCenter: CGPoint!
    var playButtonCenter: CGPoint!
    var CameraButtonCenter: CGPoint!
    
    var player:AVAudioPlayer = AVAudioPlayer()
    
    override func viewDidLoad() {
        //Jouer la musique
        do {
            let audioPlayer = Bundle.main.path(forResource: "song", ofType: "wav")
            try player = AVAudioPlayer(contentsOf: NSURL(fileURLWithPath: audioPlayer!) as URL)
        }
        catch{
            //AFFICHER LES ERREURS
            print(error)
        }
        
        super.viewDidLoad()
        
        // Do any additional setup after loading the view, typically from a nib.
        bp_playButtonCenter = bp_play.center
        exitButtonCenter = exit.center
        scoreButtonCenter = score.center
        playButtonCenter = playButton.center
        CameraButtonCenter = cameraButton.center
        
        // Positionnement des bouttons au debut egal à celle de more
        bp_play.center = more.center
        exit.center = more.center
        score.center = more.center
        playButton.center = more.center
        cameraButton.center = more.center
        
    }
    
    //Fonction pour stoper la musique
    @IBAction func Play(_ sender: Any) {
        player.stop()
    }
    
    @IBAction func CameraCliked(_ sender: UIButton) {
        print("Camera pressed")
        self.performSegue(withIdentifier: "CameraSegue", sender: self)
        sender.pulsate()
    }
    
    @IBAction func moreClicked(_ sender: UIButton) {
        if buttonsAreOn{
            UIView.animate(withDuration: 0.3, delay: 0, options: .autoreverse, animations: {
                self.hideButtons()
            }, completion: nil)
        } else{
            UIView.animate(withDuration: 0.3, delay: 0, options: .autoreverse, animations: {
                self.showButtons()
            }, completion: nil)
        }
        buttonsAreOn = !buttonsAreOn
        player.play()
        sender.pulsate()
    }
    private func hideButtons(){
        
        self.bp_play.alpha = 1
        self.exit.alpha = 1
        self.score.alpha = 1
        self.playButton.alpha = 1
        self.cameraButton.alpha = 1
        
        self.bp_play.center = self.bp_playButtonCenter
        self.exit.center = self.exitButtonCenter
        self.score.center = self.scoreButtonCenter
        self.playButton.center = self.playButtonCenter
        self.cameraButton.center = self.CameraButtonCenter
    }
    private func showButtons(){
        
        self.bp_play.alpha = 0
        self.exit.alpha = 0
        self.score.alpha = 0
        self.playButton.alpha = 0
        self.cameraButton.alpha = 0
        
        self.bp_play.center = self.bp_playButtonCenter
        self.exit.center = self.exitButtonCenter
        self.score.center = self.scoreButtonCenter
        self.playButton.center = self.playButtonCenter
         self.cameraButton.center = self.CameraButtonCenter
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func onClickPlay(_ sender: UIButton) {
        
    }
    
    @IBAction func onClickHighScores(_ sender: Any) {
    }
    
    @IBAction func soundMute(_ sender: UIButton) {
        sender.flash()
    }
    @IBAction func onClickQuit(_ sender: Any) {
        print("Localisation a été bien chargé")
        self.performSegue(withIdentifier: "LocalisationSegue", sender: self)
    }
}

