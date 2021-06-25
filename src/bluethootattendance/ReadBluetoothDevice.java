/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluethootattendance;

import java.io.IOException;
import java.util.ArrayList;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

/**
 *
 * @author Administrator
 */
public class ReadBluetoothDevice {
    public ArrayList getDevices() {
        /* Create Vector variable */
        final ArrayList devicesDiscovered = new ArrayList();
        try {
            final Object inquiryCompletedEvent = new Object();
            /* Clear Vector variable */
            devicesDiscovered.clear();

            /* Create an object of DiscoveryListener */
            DiscoveryListener listener = new DiscoveryListener() {

                public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                    /* Get devices paired with system or in range(Without Pair) */
                    //devicesDiscovered.addElement(btDevice);
                    //devicesDiscovered.add(btDevice);
                     try {
                    /* Add bluetooth device name and address in list */
                    devicesDiscovered.add(btDevice.getFriendlyName(false));
                    //devicesDiscovered.add(btDevice.getBluetoothAddress());
                } catch (IOException e) {
                }
                    
                }

                public void inquiryCompleted(int discType) {
                    /* Notify thread when inquiry completed */
                    synchronized (inquiryCompletedEvent) {
                        inquiryCompletedEvent.notifyAll();
                    }
                }

                // To find service on bluetooth 
                public void serviceSearchCompleted(int transID, int respCode) {
                }

                // To find service on bluetooth 
                public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                }
            };

            synchronized (inquiryCompletedEvent) {
                /* Start device discovery */
                boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
                if (started) {
                    System.out.println("wait for device inquiry to complete...");
                    inquiryCompletedEvent.wait();
                }
            }
        } catch (InterruptedException | BluetoothStateException e) {
        }
        /* Return list of devices */
        return devicesDiscovered;
    }
}
