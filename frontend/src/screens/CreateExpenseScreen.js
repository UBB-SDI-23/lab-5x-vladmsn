import axios from "../axios";
import React, { useState } from "react";
import { Button, Col, Container, Form, Row, Toast, ToastContainer} from "react-bootstrap";
import { useContext } from "react";
import { AuthContext } from "../AuthContext";
import { useNavigate } from "react-router-dom";
import { useParams } from "react-router-dom";

const CreateGroupScreen = () => {
    const { id } = useParams();
    const [ammount, setAmmount] = useState(0);
    const [description, setDescription] = useState("");
    const [currency, setCurrency] = useState("");
    const [category , setCategory] = useState("");
    const [transactionDate, setTransactionDate] = useState("");
    
    const [showToast, setShowToast] = useState(false);
    const [toastMessage, setToastMessage] = useState("");
    const userInfo = useContext(AuthContext).userInfo;
    const navigate = useNavigate();

    const handleToastClose = () => {
      setShowToast(false);
      navigate(-1);
    };
  
    const handleSubmit = async (e) => {
      e.preventDefault();
  
      try {
        const response = await axios.post("/api/v1/expenses", {
            groupId: id,
            payerId: userInfo.userId,
            amount: ammount,
            description: description,
            currency: currency,
            category: category,
            transactionDate: transactionDate ? transactionDate : new Date(),
          });

          if (response.status === 201) {
            setToastMessage("Expense added successfully!");
            setShowToast(true);
          }
          else {
            setToastMessage("Error adding expense:" + response);
            setShowToast(true);
          }
        
      } catch (error) {
        console.error("Error adding expense:", error);
            setToastMessage("Error adding expense:");
            setShowToast(true);
      }
    };
  
    return (
      <Container>
        <Row className="justify-content-center mt-5">
          <Col xs={12} md={6}>
            <h1>Create Group</h1>
            <Form onSubmit={handleSubmit}>
              <Form.Group controlId="name">
                <Form.Label>Description</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter description"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                />
              </Form.Group>

              <Form.Group controlId="category">
                <Form.Label>Description</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter category"
                  value={category}
                  onChange={(e) => setCategory(e.target.value)}
                />
              </Form.Group>
              
              <Form.Group controlId="ammount">
                <Form.Label>Ammount</Form.Label>
                <Form.Control
                  type="double"
                  placeholder="Enter ammount"
                  value={ammount}
                  onChange={(e) => setAmmount(e.target.value)}
                />
              </Form.Group>
              <Form.Group controlId="currency">
                <Form.Label>Currency</Form.Label>
                <Form.Control
                  as="select"
                  value={currency}
                  onChange={(e) => setCurrency(e.target.value)}
                >
                  <option value="">Select currency</option>
                  <option value="USD">USD</option>
                  <option value="EUR">EUR</option>
                  <option value="GBP">GBP</option>
                  {/* Add more currency options as needed */}
                </Form.Control>
                </Form.Group>
              
                <Form.Group controlId="transactionDate">
                <Form.Label>Transaction Date</Form.Label>
                <Form.Control
                    type="date"
                    placeholder="Enter transaction date"
                    value={transactionDate}
                    onChange={(e) => setTransactionDate(e.target.value)}
                >
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