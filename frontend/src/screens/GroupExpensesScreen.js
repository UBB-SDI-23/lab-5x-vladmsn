import React from 'react'
import { useState, useEffect } from 'react';
import axios from "../axios";

import { Link, useParams } from 'react-router-dom';
import { Container, Row, Col, Card, Button} from 'react-bootstrap';
import ListGroup from "react-bootstrap/ListGroup";
import Expense from '../components/Expense';


function GroupExpensesScreen() {
    const [groupExpenses , setGroupExpenses] = useState([])
    const { id } = useParams();

    useEffect(() => {
        const fetchExpenses = async () => {
          try {
            const { data } = await axios.get(`/api/v1/expenses/group/${id}`);
          
            setGroupExpenses(data);
          } catch (error) {
            console.log(error);
          }
        };
    
        fetchExpenses();
        }, []);
    
    return (
      <Container>
        <h1>Group Expenses</h1>
        <Row className="my-3">
          <Col sm={4}>
            <Card>
              <Card.Body>
                <Link to={`/group/${id}`} className='card-link'>
                  <Card.Title>Group Information</Card.Title>
                </Link>
              </Card.Body>
            </Card>
          </Col>
          <Col sm={4}>
            <Card>
              <Card.Body>
                <Link to={`/mygroups/${id}/my-expenses`} className='card-link'>
                  <Card.Title>My Expenses</Card.Title>
                </Link>
              </Card.Body>
            </Card>
          </Col>
          <Col sm={4}>
            <Card>
              <Card.Body>
                <Link to={`/mygroups/${id}/statistics`} className='card-link'>
                  <Card.Title>Statistics</Card.Title>
                </Link>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      <Row>
      <Row className="justify-content-center">
        <Col xs={12} md={8} lg={6}>
          <h1>Expenses</h1>
        </Col>
        <Col xs={12} md={4} lg={6} className="d-flex justify-content-end">
          <Button variant="primary" href="/expense/create"> + </Button>
        </Col>
      </Row>
        <div className="d-flex justify-content-center">
            <ListGroup className="w-100">
            {groupExpenses.map((expense) => (
                <ListGroup.Item key={expense.id} className="border-0">
                <Expense expense={expense} />
                </ListGroup.Item>
            ))}
            </ListGroup>
        </div>
      </Row>
      </Container>
    );
}

export default GroupExpensesScreen