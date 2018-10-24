//
//  Highscores.swift
//  Piano Tiles
//
//  Created by Combo Kamarouzamane on 24/10/2018.
//  Copyright © 2018 Combo Kamarouzamane. All rights reserved.
//

import UIKit
import CoreData

class Highscores: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var tableHighscores: UITableView!
    
    var lastHighscoreID = ""
    var lastHighscoreTime = ""
    var arrayHighscore = [NSManagedObject]()
    var newScore = false

    override func viewDidLoad() {
        super.viewDidLoad()
        if(newScore) {
            save(time: lastHighscoreTime, id: lastHighscoreID)
        }
        tableHighscores.delegate = self
        tableHighscores.dataSource = self
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        guard let appDelegate = UIApplication.shared.delegate as? AppDelegate else { return }
        let managedContext = appDelegate.persistentContainer.viewContext
        let fetchRequest = NSFetchRequest<NSManagedObject>(entityName: "Score")
        let sort = NSSortDescriptor(key: "time", ascending: true)
        fetchRequest.sortDescriptors = [sort]
        
        do {
            arrayHighscore = try managedContext.fetch(fetchRequest)
        } catch let err as NSError {
            print("failled to fetch", err)
        }
    }
    
    func save(time: String, id: String) {
        guard let appDelegate = UIApplication.shared.delegate as? AppDelegate else { return }
        let managedContext = appDelegate.persistentContainer.viewContext
        let score = NSEntityDescription.entity(forEntityName: "Score", in: managedContext)!
        let item = NSManagedObject(entity: score, insertInto: managedContext)
        item.setValue(time, forKey: "time")
        item.setValue(id, forKey: "id")
        
        do {
            try managedContext.save()
            arrayHighscore.append(item)
        } catch let err as NSError {
            print("failled to save", err)
        }
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return arrayHighscore.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableHighscores.dequeueReusableCell(withIdentifier: "cell")
        let item = arrayHighscore[indexPath.row]
        cell?.textLabel?.text = item.value(forKeyPath: "time") as? String
        cell?.detailTextLabel?.text = item.value(forKeyPath: "id") as? String
        return cell!
    }
    
    func tableView(_ tableView: UITableView, editActionsForRowAt indexPath: IndexPath) -> [UITableViewRowAction]? {
        let delAction = UITableViewRowAction(style: UITableViewRowActionStyle.default, title: "Delete") {(action, index) in
            guard let appDelegate = UIApplication.shared.delegate as? AppDelegate else { return }
            let managedContext = appDelegate.persistentContainer.viewContext
            let item = self.arrayHighscore[indexPath.row]
            managedContext.delete(item)
            self.arrayHighscore.remove(at: indexPath.row)
            tableView.deleteRows(at: [indexPath], with: UITableViewRowAnimation.automatic)
            
            do {
                try managedContext.save()
            } catch let err as NSError {
                print("failled to delete", err)
            }
        }
        delAction.backgroundColor = UIColor.orange
        return [delAction]
    }
}
