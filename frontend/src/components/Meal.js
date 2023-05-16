import React from "react";
import { Card } from "react-bootstrap";

const Meal = ({ meal }) => {
  return (
    <Card className="my-3 p-3 rounded">
      <Card.Body>
        <Card.Title as="h4">
          <strong>{meal.name}</strong>
        </Card.Title>
        <Card.Text as="h6">type: {meal.type}</Card.Text>
        <Card.Text as="h6">calories: {meal.calories}</Card.Text>
        <Card.Text as="h6">date: {meal.date}</Card.Text>
        <Card.Text as="h6">notes: {meal.notes}</Card.Text>
      </Card.Body>
    </Card>
  );
};

export default Meal;
