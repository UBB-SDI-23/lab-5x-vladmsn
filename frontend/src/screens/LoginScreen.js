import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Card, Container, Form, Button } from "react-bootstrap";
import { AuthContext } from "../AuthContext";
import axios from "../axios";

const LoginScreen = () => {
  const {login} = React.useContext(AuthContext);    
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
        var response = await axios.post("/api/v1/auth/login", {
            username: username,
            password: password
        });

        if (response.status === 200) {
            console.log("User logged in:", response.data);

            localStorage.setItem("token", response.data.access_token);
            login(response.data.access_token);
            navigate("/");
        }
    } catch (error) {
        console.error("Error logging in:", error);
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center vh-100">
      <Card className="w-50">
        <Card.Body>
          <h2>Login</h2>
          <Form onSubmit={handleSubmit}>
            <Form.Group controlId="formUsername">
              <Form.Label>username</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
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

            <Button variant="primary" type="submit" className="my-2">
              Sign In
            </Button>
          </Form>

          <div className="mt-3">
            Don't have an account? <Link to="/register">Sign up</Link>
          </div>
        </Card.Body>
      </Card>
    </Container>
  );
};

export default LoginScreen;