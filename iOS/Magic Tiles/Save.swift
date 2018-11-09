//
//  Save.swift
//  Piano Tiles
//
//  Created by Hajanirina Randimbisoa on 22/10/2018.
//  Copyright © 2018 Alexandre Gresset. All rights reserved.
//

import UIKit

class Save: UIViewController {
    
    @IBOutlet weak var labTemps: UILabel!
    @IBOutlet weak var textFieldPseudo: UITextField!
    
    var score = 0.0
    var dateString = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        labTemps.text = String(format:"%.2f", score)+"s"
        let date = Date()
        let calendar = Calendar.current
        let day = calendar.component(.day, from: date)
        let month = calendar.component(.month, from: date)
        let year = calendar.component(.year, from: date)
        let hour = calendar.component(.hour, from: date)
        let minutes = calendar.component(.minute, from: date)
        
        
        var dayString = String(day)
        if(day < 10) { dayString = "0" + String(day) }
        
        var monthString = String(month)
        if(month < 10) { monthString = "0" + String(month) }
        
        var hourString = String(hour)
        if(hour < 10) { hourString = "0" + String(hour) }
        
        var minutesString = String(minutes)
        if(minutes < 10) { minutesString = "0" + String(minutes)}
        
        dateString = "   \(dayString)/\(monthString)/\(year) \(hourString):\(minutesString)"
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func endTextEditing(_ sender: Any) {
        
    }
    
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "highscoresSegue") {
            let highscores = segue.destination as! Highscores
            highscores.lastHighscoreTime = String(format:"%.2f", score)+"s"
            highscores.lastHighscoreID = textFieldPseudo.text! + dateString
            highscores.newScore = true
        }
    }
    
}

