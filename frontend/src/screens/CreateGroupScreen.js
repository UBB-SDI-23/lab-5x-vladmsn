import axios from "axios";
import React, { useState } from "react";
import { Button, Col, Container, Form, Row } from "react-bootstrap";

const CreateGroupScreen = () => {
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [preferredCurrency, setPreferredCurrency] = useState("");
  
    const handleNameChange = (e) => {
      setName(e.target.value);
    };
  
    const handleDescriptionChange = (e) => {
      setDescription(e.target.value);
    };
  
    const handleCurrencyChange = (e) => {
      setPreferredCurrency(e.target.value);
    };
  
    const handleSubmit = async (e) => {
      e.preventDefault();
  
      try {
        const response = await axios.post("/api/v1/groups", {
            name: name,
            description: description,
            preferredCurrency: preferredCurrency,
            createdAt: new Date(),
            updatedAt: new Date()
          });
        console.log("Group created:", response.data);
        // Handle success response or perform other actions
      } catch (error) {
        console.error("Error creating group:", error);
        // Handle error response or display error message
      }
    };
  
    return (
      <Container>
        <Row className="justify-content-center mt-5">
          <Col xs={12} md={6}>
            <h1>Create Group</h1>
            <Form onSubmit={handleSubmit}>
              <Form.Group controlId="name">
                <Form.Label>Name</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter group name"
                  value={name}
                  onChange={handleNameChange}
                />
              </Form.Group>
              <Form.Group controlId="description">
                <Form.Label>Description</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter group description"
                  value={description}
                  onChange={handleDescriptionChange}
                />
              </Form.Group>
              <Form.Group controlId="currency">
                <Form.Label>Currency</Form.Label>
                <Form.Control
                  as="select"
                  value={preferredCurrency}
                  onChange={handleCurrencyChange}
                >
                  <option value="">Select currency</option>
                  <option value="USD">USD</option>
                  <option value="EUR">EUR</option>
                  <option value="GBP">GBP</option>
                  {/* Add more currency options as needed */}
                </Form.Control>
              </Form.Group>
              <Button variant="primary" type="submit">
                Create
              </Button>
            </Form>
          </Col>
        </Row>
      </Container>
    );
  };
  
  export default CreateGroupScreen;