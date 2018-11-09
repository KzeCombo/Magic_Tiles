//
//  Localisation.swift
//  Magic Tiles
//
//  Created by Ando RANDRIAMARO on 05/11/2018.
//  Copyright © 2018 Hajanirina Randimbisoa. All rights reserved.
//

import UIKit
import MapKit
import CoreLocation
class Localisation: UIViewController, CLLocationManagerDelegate {
    
    
    @IBAction func AcceuilBouton(_ sender: UIButton) {
        print("Revenir à l'acceuil")
        self.performSegue(withIdentifier: "AcceuilSegue", sender: self)
    }
    
    
    @IBOutlet weak var myMap: MKMapView!
    
    let manager = CLLocationManager()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //Configurer mon manager
        manager.delegate = self
        //Propriété pour la localisation precise et meilleur
        manager.desiredAccuracy = kCLLocationAccuracyBest
        //Demande d'autorisation pour l'utilisateur a l'aide d'un pop
        manager.requestWhenInUseAuthorization()
        //Demarage de mode GPS
        manager.startUpdatingLocation()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        // test locations print(locations)
        //afficher la location exacte ou bien un tracer simple
        let location = locations[0]
        //Precision de la carte
        let span: MKCoordinateSpan = MKCoordinateSpan(latitudeDelta: 0.01,longitudeDelta: 0.01)
        //Créer une location en 2D de ma localisation
        let myLocation: CLLocationCoordinate2D = CLLocationCoordinate2DMake(location.coordinate.latitude, location.coordinate.longitude)
        //Créer une région et configuration
        let region: MKCoordinateRegion = MKCoordinateRegion(center: myLocation, span: span)
        //Afficher la map, et animer la map
        myMap.setRegion(region, animated: true)
        //Afficher le petit point bleu sur la carte
        self.myMap.showsUserLocation = true
        
    }
    
    
    
}
