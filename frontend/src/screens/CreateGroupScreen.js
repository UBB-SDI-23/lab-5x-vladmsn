import axios from "../axios";
import React, { useState } from "react";
import { Button, Col, Container, Form, Row, Toast, ToastContainer} from "react-bootstrap";
import { useContext } from "react";
import { AuthContext } from "../AuthContext";
import { useNavigate } from "react-router-dom";

const CreateGroupScreen = () => {
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [preferredCurrency, setPreferredCurrency] = useState("");
    const [showToast, setShowToast] = useState(false);
    const [toastMessage, setToastMessage] = useState("");
    const userInfo = useContext(AuthContext).userInfo;
    const navigate = useNavigate();

    const handleToastClose = () => {
      setShowToast(false);
      navigate("/mygroups");
    };
  
    const handleNameChange = (e) => {
      setName(e.target.value);
    };
  
    const handleDescriptionChange = (e) => {
      if (e.target.value.length === 0) {
        
        setDescription("No description provided");
        return;
      }
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

          const groupId = response?.data.id;

          console.log(groupId);
          console.log(userInfo.userId);
          
          const response2 = await axios.post(`/api/v1/groups/participant?groupId=${groupId}&userId=${userInfo.userId}`);

          if (response2.status === 201) {
            setToastMessage("Group created successfully!");
            setShowToast(true);
          }
          else {
            setToastMessage("Error creating group:" + response);
            setShowToast(true);
          }
        
      } catch (error) {
        console.error("Error creating group:", error);
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

        <ToastContainer position="middle-center">
        {showToast && (
          <Toast onClose={handleToastClose}>
            <Toast.Header>
              <strong className="me-auto">Success!</strong>
              <small>Just now</small>
            </Toast.Header>
            <Toast.Body>{toastMessage}</Toast.Body>
          </Toast>
        )}
      </ToastContainer>
      </Container>
    );
  };
  
  export default CreateGroupScreen;