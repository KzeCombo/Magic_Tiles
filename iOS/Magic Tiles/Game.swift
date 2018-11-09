//
//  Game.swift
//  Piano Tiles
//
//  Created by Hajanirina Randimbisoa on 21/10/2018.
//  Copyright © 2018 Hajanirina Randimbisoa. All rights reserved.
//

import UIKit
import AVFoundation
class Game: UIViewController, AVAudioPlayerDelegate {
    
    @IBOutlet weak var bp_back: UIButton!
    @IBOutlet weak var lab_timer: UILabel!
    @IBOutlet weak var bp_save: UIButton!
    
    var counter = 0.0
    var timer = Timer()
    var hardMode = false
    
    var audioPlayer = AVAudioPlayer()
    let soundArray = ["song"]//["song1","song2","song3","song4","song5","song6","song7","song8","song9","song10","song11","song12","song13","song14","song15","song16", ]
    
    @IBOutlet weak var hardModeSwitch: UISwitch!
    @IBOutlet weak var hardModeLabel: UILabel!
    
    @IBOutlet weak var bp_00: UIButton!
    @IBOutlet weak var bp_01: UIButton!
    @IBOutlet weak var bp_02: UIButton!
    @IBOutlet weak var bp_03: UIButton!
    
    @IBOutlet weak var bp_10: UIButton!
    @IBOutlet weak var bp_11: UIButton!
    @IBOutlet weak var bp_12: UIButton!
    @IBOutlet weak var bp_13: UIButton!
    
    @IBOutlet weak var bp_20: UIButton!
    @IBOutlet weak var bp_21: UIButton!
    @IBOutlet weak var bp_22: UIButton!
    @IBOutlet weak var bp_23: UIButton!
    
    @IBOutlet weak var bp_30: UIButton!
    @IBOutlet weak var bp_31: UIButton!
    @IBOutlet weak var bp_32: UIButton!
    @IBOutlet weak var bp_33: UIButton!
    
    var noteLigne0: [UIButton] = []
    var noteLigne1: [UIButton] = []
    var noteLigne2: [UIButton] = []
    var noteLigne3: [UIButton] = []
    
    var partition: [Int] = []
    var avancement = 0
    let avancementMax = 63
    var isPlaying = true
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
    
        
        avancement = 0
        counter = 0.0
        partition = []
        isPlaying = true
        
        hardModeSwitch.isHidden = false
        hardModeLabel.isHidden = false
        
        lab_timer.text = String(counter)
        bp_save.isHidden = true
        bp_back.isHidden = false
        bp_00.setTitle("", for: .normal)
        
        bp_00.layer.borderWidth = 2
        bp_01.layer.borderWidth = 2
        bp_02.layer.borderWidth = 2
        bp_03.layer.borderWidth = 2
        bp_10.layer.borderWidth = 2
        bp_11.layer.borderWidth = 2
        bp_12.layer.borderWidth = 2
        bp_13.layer.borderWidth = 2
        bp_20.layer.borderWidth = 2
        bp_21.layer.borderWidth = 2
        bp_22.layer.borderWidth = 2
        bp_23.layer.borderWidth = 2
        bp_30.layer.borderWidth = 2
        bp_31.layer.borderWidth = 2
        bp_32.layer.borderWidth = 2
        bp_33.layer.borderWidth = 2
        
        bp_00.layer.cornerRadius = 23
        bp_01.layer.cornerRadius = 23
        bp_02.layer.cornerRadius = 23
        bp_03.layer.cornerRadius = 23
        bp_10.layer.cornerRadius = 23
        bp_11.layer.cornerRadius = 23
        bp_12.layer.cornerRadius = 23
        bp_13.layer.cornerRadius = 23
        bp_20.layer.cornerRadius = 23
        bp_21.layer.cornerRadius = 23
        bp_22.layer.cornerRadius = 23
        bp_23.layer.cornerRadius = 23
        bp_30.layer.cornerRadius = 23
        bp_31.layer.cornerRadius = 23
        bp_32.layer.cornerRadius = 23
        bp_33.layer.cornerRadius = 23
        
        noteLigne0 = [bp_00, bp_01, bp_02, bp_03]
        noteLigne1 = [bp_10, bp_11, bp_12, bp_13]
        noteLigne2 = [bp_20, bp_21, bp_22, bp_23]
        noteLigne3 = [bp_30, bp_31, bp_32, bp_33]
      
        for _ in 0...avancementMax {
            partition.append(Int(arc4random_uniform(4)))
        }
        
        setNote(noteLigne: noteLigne0, avancementNote: 0)
        setNote(noteLigne: noteLigne1, avancementNote: 1)
        setNote(noteLigne: noteLigne2, avancementNote: 2)
        setNote(noteLigne: noteLigne3, avancementNote: 3)
    }

    @IBAction func hardModFct(_ sender: Any) {
        if(hardModeSwitch.isOn) {
            hardMode = true
        }
        else {
            hardMode = false
        }
        setNote(noteLigne: noteLigne0, avancementNote: 0)
        setNote(noteLigne: noteLigne1, avancementNote: 1)
        setNote(noteLigne: noteLigne2, avancementNote: 2)
        setNote(noteLigne: noteLigne3, avancementNote: 3)
    }
    
    
    func setNote(noteLigne: [UIButton], avancementNote: Int) {
        if(hardMode == false) {
            noteLigne[0].backgroundColor = UIColor(red: 255/255.0, green: 21/255.0, blue: 133/255.0, alpha: 1.0)
            noteLigne[1].backgroundColor = UIColor(red: 138/255.0, green: 43/255.0, blue: 226/255.0, alpha: 1.0)
            noteLigne[2].backgroundColor = UIColor(red: 60/255.0, green: 179/255.0, blue: 113/255.0, alpha: 1.0)
            noteLigne[3].backgroundColor = UIColor(red: 255/255.0, green: 215/255.0, blue: 0/255.0, alpha: 1.0)
            
            var colorNote = UIColor.black
            
            if(avancementNote == 0 || avancementNote == avancementMax-1) {
                colorNote = UIColor.blue
                
            }
            
            if(avancementNote<avancementMax) {
                switch partition[avancementNote] {
                case 0:
                    noteLigne[0].backgroundColor = colorNote
                case 1:
                    noteLigne[1].backgroundColor = colorNote
                case 2:
                    noteLigne[2].backgroundColor = colorNote
                case 3:
                    noteLigne[3].backgroundColor = colorNote
                default:
                    print("erreur switch")
                }
            }
        }
        else {
            noteLigne[0].backgroundColor = UIColor.black
            noteLigne[1].backgroundColor = UIColor.black
            noteLigne[2].backgroundColor = UIColor.black
            noteLigne[3].backgroundColor = UIColor.black
            
            var colorNote = UIColor.white
            
            if(avancementNote == 0 || avancementNote == avancementMax-1) {
                colorNote = UIColor.blue
            }
            
            if(avancementNote<avancementMax) {
                switch partition[avancementNote] {
                case 0:
                    noteLigne[0].backgroundColor = colorNote
                case 1:
                    noteLigne[1].backgroundColor = colorNote
                case 2:
                    noteLigne[2].backgroundColor = colorNote
                case 3:
                    noteLigne[3].backgroundColor = colorNote
                default:
                    print("erreur switch")
                }
            }
        }
    }
    
    func frappeOK()  {
        avancement += 1
        setNote(noteLigne: noteLigne0, avancementNote: avancement)
        setNote(noteLigne: noteLigne1, avancementNote: avancement+1)
        setNote(noteLigne: noteLigne2, avancementNote: avancement+2)
        setNote(noteLigne: noteLigne3, avancementNote: avancement+3)
    }
    
    func chronoStart()  {
        bp_back.isHidden = true
        hardModeSwitch.isHidden = true
        hardModeLabel.isHidden = true
        timer = Timer.scheduledTimer(withTimeInterval: 0.01, repeats: true) { _ in
            self.counter += 0.01
            self.lab_timer.text = String(format:"%.2f", self.counter)
        }
    }
    
    func chronoStop()  {
        isPlaying = false
        timer.invalidate()
        bp_back.isHidden = false
        bp_save.isHidden = false
        bp_00.setTitle("REPEAT", for: .normal)
        
    }

    
    @IBAction func frappeBP00(_ sender: UIButton) {
        if(isPlaying) {
            if(hardMode==false && bp_00.backgroundColor == UIColor.black || bp_00.backgroundColor == UIColor.blue) {
                if(avancement == 0) { chronoStart() }
                if(avancement == avancementMax-1) { chronoStop() }
                frappeOK()
                sender.pulsate()
            }
            else {
                if(hardMode==true && bp_00.backgroundColor == UIColor.white || bp_00.backgroundColor == UIColor.blue) {
                    if(avancement == 0) { chronoStart() }
                    if(avancement == avancementMax-1) { chronoStop() }
                    frappeOK()
                    sender.pulsate()
                }
                else {
                    bp_00.backgroundColor = UIColor.red
                    chronoStop()
                    sender.shake()
                    bp_save.isHidden = true
                }
            }
        }
        else { viewDidLoad() }
    }
    
    @IBAction func frappeBP01(_ sender: UIButton) {
        if(isPlaying) {
            if(hardMode==false && bp_01.backgroundColor == UIColor.black || bp_01.backgroundColor == UIColor.blue) {
                if(avancement == 0) { chronoStart() }
                if(avancement == avancementMax-1) { chronoStop() }
                frappeOK()
                sender.flash()
            }
            else {
                if(hardMode==true && bp_01.backgroundColor == UIColor.white || bp_01.backgroundColor == UIColor.blue) {
                    if(avancement == 0) { chronoStart() }
                    if(avancement == avancementMax-1) { chronoStop() }
                    frappeOK()
                    sender.flash()
                }
                else {
                    bp_01.backgroundColor = UIColor.red
                    chronoStop()
                    sender.shake()
                    bp_save.isHidden = true
                }
            }
        }
    }

    @IBAction func frappeBP02(_ sender: UIButton) {
        if(isPlaying) {
            if(hardMode==false && bp_02.backgroundColor == UIColor.black || bp_02.backgroundColor == UIColor.blue) {
                if(avancement == 0) { chronoStart() }
                if(avancement == avancementMax-1) { chronoStop() }
                frappeOK()
                sender.flash()
            }
            else {
                if(hardMode==true && bp_02.backgroundColor == UIColor.white || bp_02.backgroundColor == UIColor.blue) {
                    if(avancement == 0) { chronoStart() }
                    if(avancement == avancementMax-1) { chronoStop() }
                    frappeOK()
                    sender.flash()
                }
                else {
                    bp_02.backgroundColor = UIColor.red
                    chronoStop()
                    sender.shake()
                    bp_save.isHidden = true
                }
            }
        }
    }
    
    
    @IBAction func frappeBP03(_ sender: UIButton) {
        if(isPlaying) {
            if(hardMode==false && bp_03.backgroundColor == UIColor.black || bp_03.backgroundColor == UIColor.blue) {
                if(avancement == 0) { chronoStart() }
                if(avancement == avancementMax-1) { chronoStop() }
                frappeOK()
                sender.pulsate()
            }
            else {
                if(hardMode==true && bp_03.backgroundColor == UIColor.white || bp_03.backgroundColor == UIColor.blue) {
                    if(avancement == 0) { chronoStart() }
                    if(avancement == avancementMax-1) { chronoStop() }
                    frappeOK()
                    sender.flash()
                }
                else {
                    bp_03.backgroundColor = UIColor.red
                    chronoStop()
                    sender.shake()
                    bp_save.isHidden = true
                }
            }
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "saveSegue") {
            let save = segue.destination as! Save
            save.score = counter
        }
    }
}
