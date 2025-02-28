const express = require("express");
const mongoose = require("mongoose");

const app = express();
app.use(express.json()); // Middleware to parse JSON requests

// Connect to MongoDB
mongoose.connect("mongodb://localhost:27017/medirecord", {
    useNewUrlParser: true,
    useUnifiedTopology: true,
}).then(() => console.log("MongoDB connected"))
  .catch(err => console.error("MongoDB connection error:", err));

// Define a flexible schema
const dynamicSchema = new mongoose.Schema({}, { strict: false });
const DynamicModel = mongoose.model("DynamicCollection", dynamicSchema);

// First POST route to save data
app.post("/save", async (req, res) => {
    try {
        const newData = new DynamicModel(req.body);
        const savedData = await newData.save();
        console.log(res)
        res.status(201).json({ message: "Data saved successfully", data: savedData });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Second POST route for any other operation (e.g., logging data)
app.post("/log", async (req, res) => {
    try {
        console.log("Received Data:", req.body);
        res.status(200).json({ message: "Data logged successfully", data: req.body });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Start the server
app.listen(3000, () => console.log("Server running on port 3000"));
