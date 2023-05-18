import React, { useState } from "react";
import { Link } from "react-router-dom";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";

import axios from "../axios";

const SignupScreen = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [preferredCurrency, setPreferredCurrency] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (password !== confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    try {
        const response = await axios.post("/api/v1/auth/register", {
            username: username,
            email: email,
            password: password,
            preferredCurrency: preferredCurrency,
            firstName: firstName,
            lastName: lastName
        });

        console.log("User created:", response.data);
        } catch (error) {
            console.error("Error creating user:", error);
        }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center vh-100">
      <Card className="w-50">
        <Card.Body>
          <h2>Sign Up</h2>

          {error && <Alert variant="danger">{error}</Alert>}

          <Form onSubmit={handleSubmit}>
            <Form.Group controlId="formUsername">
              <Form.Label>username</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter username"
                value={email}
                onChange={(e) => setUsername(e.target.value)}
              />
            </Form.Group>

            <Form.Group controlId="formEmail">
              <Form.Label>Email address</Form.Label>
              <Form.Control
                type="email"
                placeholder="Enter email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </Form.Group>

            <Form.Group controlId="currency">
                <Form.Label>Currency</Form.Label>
                <Form.Control
                  as="select"
                  value={preferredCurrency}
                  onChange={(e) => setPreferredCurrency(e.target.value)}
                >
                  <option value="">Select currency</option>
                  <option value="USD">USD</option>
                  <option value="EUR">EUR</option>
                  <option value="GBP">GBP</option>
                  {/* Add more currency options as needed */}
                </Form.Control>
            </Form.Group>

            <Form.Group controlId="formFirstName">
                <Form.Label>First Name</Form.Label>
                <Form.Control
                    type="text"
                    placeholder="Enter first name"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                />
            </Form.Group>

            <Form.Group controlId="formLastName">
                <Form.Label>Last Name</Form.Label>
                <Form.Control
                    type="text"
                    placeholder="Enter last name"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                />
            </Form.Group>

            <Form.Group controlId="formPassword">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </Form.Group>

            <Form.Group controlId="formConfirmPassword">
              <Form.Label>Confirm Password</Form.Label>
              <Form.Control
                type="password"
                placeholder="Confirm Password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
              />
            </Form.Group>

            <Button className="my-2" variant="primary" type="submit">
              Sign Up
            </Button>
          </Form>

          <div className="mt-3">
            Already have an account? <Link to="/login">Sign in</Link>
          </div>
        </Card.Body>
      </Card>
    </Container>
  );
};

export default SignupScreen;