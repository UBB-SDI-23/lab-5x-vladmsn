import React from "react";

import { Card, Col, Row } from "react-bootstrap";
import { Link } from "react-router-dom";


const Expense = ({ expense }) => {
    const getInitials = (firstName, lastName) => {
        return `${firstName.charAt(0)}${lastName.charAt(0)}`;
      };
    
      return (
        <Card className="my-3 p-3 rounded">
            <Card.Body>
                <Row className="align-items-center">
                <Col xs="auto" className="mr-3">
                    <Link to={`/user/${expense.payer.id}`} className="card-link">
                    <div className="user-icon">{getInitials(expense.payer.firstName, expense.payer.lastName)}</div>
                    </Link>
                </Col>
                <Col>
                    <Link to={`/expense/${expense.id}`} className="card-link">
                    <Card.Title as="h5" >{expense.description}</Card.Title>
                    </Link>
                    <Card.Text as="h6">Payed by: {expense.payer.firstName} {expense.payer.lastName}</Card.Text>
                </Col>
                <Col xs="auto" className="ml-auto">
                    <strong>{expense.amount.toFixed(2)} {expense.currency}</strong>
                    <Card.Text as="h6">{new Date(expense.transactionDate).toLocaleDateString("en-GB")}</Card.Text>
                </Col>
                </Row>
            </Card.Body>
        </Card>
  );
}

export default Expense;