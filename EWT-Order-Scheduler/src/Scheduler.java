import java.time.LocalTime;

/**
 * Model.
 * Sets the shift times and number of workers for each shift.
 */
public class Scheduler {

    /**
     * Sealing/Delivery shift.
     */
    static Shift sealDel = new Shift();

    /**
     * Cooking shift.
     */
    static Shift cooking = new Shift();

    /**
     * Leave by time for cooking shift.
     */
    static LocalTime leaveByTime;

    /**
     * Sets the shift times and number of workers for sealing/delivery shift(s).
     * @param numDrinks: the number in drinks in the order.
     * @param deliveryTime: time to be delivered as a LocalTime object.
     */
     public static void scheduleSealingAndDelivery(int numDrinks, LocalTime deliveryTime) {

        // Ideal delivery time is 5 minutes before deliveryTime.
        LocalTime iDeliveryTime = deliveryTime.minusMinutes(5);

        // End of sealing/delivery shift is 25 mins after ideal delivery time.
        sealDel.setTimeEnd(iDeliveryTime.plusMinutes(25));

        // Leave by time is 15 mins before the ideal delivery time -- or 20 minutes before the delivery time.
        leaveByTime = iDeliveryTime.minusMinutes(15);

        // Prep moves at a rate of ~5 drinks per minute. Round to upper multiple of 5 for extra time allowance.
        int prep = (int) Math.ceil((numDrinks / 5.0) / 5.0) * 5;
        // Sealing moves at a rate of ~10 drinks in 3 minutes. Round to upper multiple of 5 for extra time allowance.
        int sealing = (int) Math.ceil((numDrinks / (10.0/3.0)) / 5.0) * 5;
        sealDel.setTimeStart(leaveByTime.minusMinutes(prep + sealing));

        // Set numWorkers
        if (numDrinks > 200) {
            // Need 4 people so each one can take an ice chest.
            sealDel.setNumWorkers(4);
        } else if (numDrinks > 100) {
            // Need ~3 people so each one can take an ice chest.
            sealDel.setNumWorkers(3);
        } else if (numDrinks > 49) {
            // 2 people to seal. Might need 2 ice chests.
            sealDel.setNumWorkers(2);
        } else {
            // Baby order. Only 1 person.
            sealDel.setNumWorkers(1);
        }
    }

    /**
     * Sets the times and number of workers for cooking shifts.
     * @param numDrinks the number of drinks in the order.
     */
    public static void scheduleCooking(int numDrinks) {

        // Cooking shift end time is 5 minutes after sealing begins.
        cooking.setTimeEnd(sealDel.timeStart.plusMinutes(5));

        // Time start depends on the number of drinks.
        int cookingMin = 0;
        if (numDrinks >= 150) {
            // 4 boba pots.
            cookingMin = 180;
        } else if (numDrinks >= 100) {
            // 3 boba pots.
            cookingMin = 165;
        } else if (numDrinks >= 75) {
            // 2 boba pots.
            cookingMin = 150;
        } else {
            // 1 boba pot. Can boil water at the same time.
            cookingMin = 120;
        }
        cooking.setTimeStart(cooking.timeEnd.minusMinutes(cookingMin));

        // Set numWorkers.
        if (numDrinks > 300) {
            cooking.setNumWorkers(3);
        } else {
            cooking.setNumWorkers(2);
        }
    }
}
